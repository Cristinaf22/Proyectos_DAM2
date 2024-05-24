using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Windows.Forms;

namespace Directory_And_File_ForEver
{
    public partial class DirectoryAndFileForEver : Form
    {

        DirectoryInfo actualDirectory;

        bool useJsonData = false;
        List<FolderAndFile> jsonData = new List<FolderAndFile>();

        SortableBindingList<FolderAndFile> subdirectories =new SortableBindingList<FolderAndFile>();

        private static Bitmap imageEdit = new Bitmap(@"Images\\edit.png");
        private static Bitmap imageDelete = new Bitmap(@"Images\\delete.png");

        public DirectoryAndFileForEver()
        {
            InitializeComponent();
            this.FormBorderStyle = FormBorderStyle.FixedSingle;
        }

        private void buttonSearchPath_Click(object sender, EventArgs e)
        {
            FolderBrowserDialog folder = new FolderBrowserDialog();
            DialogResult result = folder.ShowDialog();

            if (result.Equals(DialogResult.OK))
            {
                textBoxPath.Text = folder.SelectedPath;

                actualDirectory = new DirectoryInfo(textBoxPath.Text);

                RefreshFolderAndFile();

                newToolStripMenuItem.Enabled = true;
                buttonSearchFilter.Enabled = true;
                useJsonData = false;

            }

        }

        private void RefreshFolderAndFile()
        {
            subdirectories.Clear();
            SetupFoldersAndFiles();
            if (useJsonData)
            {
                UpdateFromJson();
            }
            else
            {
                GetSubdirectories();
                GetFiles();
            }
            
        }

        private void UpdateFromJson()
        {
            foreach(FolderAndFile folderAndFile in jsonData)
            {
                subdirectories.Add(folderAndFile);
            }
        }

        private void GetFiles()
        {
            foreach (FileInfo file in actualDirectory.GetFiles()) 
            {
                FolderAndFile folderAndFile = new FolderAndFile
                {
                    Filename = file.Name,
                    DateModified = file.LastWriteTime,
                    Type = "File",
                    Size = bytesToText(file.Length),
                    ImgEdit = imageEdit,
                    ImgDelete = imageDelete
                };
                subdirectories.Add(folderAndFile);
            }
        }

        private String bytesToText(long bytesLen)
        {
            string[] sizes = { "B", "KB", "MB", "GB", "TB" };
            int order = 0;
            while (bytesLen >= 1024 && order < sizes.Length - 1)
            {
                order++;
                bytesLen = bytesLen / 1024;
            }

            // Adjust the format string to your preferences. For example "{0:0.#}{1}" would
            // show a single decimal place, and no space.
            string result = String.Format("{0:0.##} {1}", bytesLen, sizes[order]);
            return result;
        }

        private void GetSubdirectories()
        {
            foreach (DirectoryInfo subdir in actualDirectory.GetDirectories())
            {
                FolderAndFile folderAndFile = new FolderAndFile();
                folderAndFile.Filename = subdir.Name;
                folderAndFile.DateModified = subdir.LastWriteTime;
                folderAndFile.Type = "Folder";
                folderAndFile.Size = "";
                folderAndFile.ImgEdit = imageEdit;
                folderAndFile.ImgDelete = imageDelete;
                subdirectories.Add(folderAndFile);

            }
        }

        private void SetupFoldersAndFiles()
        {
            dataGridViewFiles.DataSource = subdirectories;
            dataGridViewFiles.Columns[0].HeaderText = "Name";
            dataGridViewFiles.Columns[0].SortMode = DataGridViewColumnSortMode.Automatic;

            dataGridViewFiles.Columns[1].HeaderText = "Date Modified";
            dataGridViewFiles.Columns[1].SortMode = DataGridViewColumnSortMode.Automatic;

            dataGridViewFiles.Columns[2].HeaderText = "Type";
            dataGridViewFiles.Columns[2].SortMode = DataGridViewColumnSortMode.Automatic;

            dataGridViewFiles.Columns[3].HeaderText = "Size";
            dataGridViewFiles.Columns[3].SortMode = DataGridViewColumnSortMode.Automatic;

            dataGridViewFiles.Columns[4].HeaderText = "Edit";
            dataGridViewFiles.Columns[4].Resizable = DataGridViewTriState.False;
            dataGridViewFiles.Columns[4].SortMode = DataGridViewColumnSortMode.NotSortable;

            dataGridViewFiles.Columns[5].HeaderText = "Delete";
            dataGridViewFiles.Columns[5].Resizable = DataGridViewTriState.False;
            dataGridViewFiles.Columns[5].SortMode = DataGridViewColumnSortMode.NotSortable;
        }

        private void FolderToolStripMenuItem_Click(object sender, EventArgs e)
        {
            string newFolderName = GetNewFileFolderName();

            if (newFolderName != null)
            {
                actualDirectory.CreateSubdirectory(newFolderName);
                RefreshFolderAndFile();
            }
        }
        private void FileToolStripMenuItem1_Click(object sender, EventArgs e)
        {
            string newFileName = GetNewFileFolderName();
            if (newFileName != null)
            {
                File.Create(Path.Combine(actualDirectory.FullName, newFileName)).Close();
                RefreshFolderAndFile();
            }
        }

        private String GetNewFileFolderName()
        {
            GetNewFileFolderDialog testDialog = new GetNewFileFolderDialog();
            String name = null;

            // Show testDialog as a modal dialog and determine if DialogResult = OK.
            if (testDialog.ShowDialog(this) == DialogResult.OK)
            {
                // Read the contents of testDialog's TextBox.
                name = testDialog.textBoxAddFolder.Text;
            }
            testDialog.Dispose();
            return name;
        }
        private void textBoxPath_KeyUp(object sender, KeyEventArgs e)
        {
            if (e.KeyCode == Keys.Enter)
            {
                if (Directory.Exists(textBoxPath.Text))
                {
                    actualDirectory = new DirectoryInfo(textBoxPath.Text);

                    RefreshFolderAndFile();

                    newToolStripMenuItem.Enabled = true;

                    useJsonData = false;
                }
                else
                {
                    MessageBox.Show("The path is invalid","Error",
                        MessageBoxButtons.OK,MessageBoxIcon.Error);
                    newToolStripMenuItem.Enabled = false;
                }
            }
        }
        private void ExitToolStripMenuItem_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void dataGridViewFiles_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            if (useJsonData)
            {
                MessageBox.Show("You are reading Json, you can´t do that", "Error", MessageBoxButtons.OK, 
                    MessageBoxIcon.Hand);
                return;
            }

            // Editar
            if (e.ColumnIndex == 4)
            {
                EditFolderOrFile(e.RowIndex);
            }

            // Borrar
            if (e.ColumnIndex == 5)
            {
                DeleteFolderOrFile(e.RowIndex);

            }
        }
        private String GetNewName()
        {
            GetNewNameDialog testDialog = new GetNewNameDialog();
            String newName = null;

            // Show testDialog as a modal dialog and determine if DialogResult = OK.
            if (testDialog.ShowDialog(this) == DialogResult.OK)
            {
                // Read the contents of testDialog's TextBox.
                newName = testDialog.textBoxRename.Text;
            }
            testDialog.Dispose();
            return newName;
        }

        private void EditFolderOrFile(int rowIndex)
        {
            String newFileName = GetNewName();
            if (newFileName != null)
            {

                FolderAndFile editElement = subdirectories[rowIndex];
                String currentPath = Path.Combine(actualDirectory.FullName, editElement.Filename);
                String newPath = Path.Combine(actualDirectory.FullName, newFileName);
                if (editElement.Type == "Folder")
                {
                    Directory.Move(currentPath,newPath);
                } else
                {
                    File.Move(currentPath,newPath);
                }
                RefreshFolderAndFile();
            }
        }

        private void DeleteFolderOrFile(int rowIndex)
        {
            if (MessageBox.Show("Are you sure you want to delete this folder/file", "Delete",
                MessageBoxButtons.YesNo, MessageBoxIcon.Exclamation) == DialogResult.Yes)
            {

                FolderAndFile deleteElement = subdirectories[rowIndex];
                if (deleteElement.Type == "Folder")
                {
                    Directory.Delete(Path.Combine(actualDirectory.FullName, deleteElement.Filename));
                }
                else
                {
                    File.Delete(Path.Combine(actualDirectory.FullName, deleteElement.Filename));
                }
                RefreshFolderAndFile();
            }
        }

        private void buttonSearchFilter_Click(object sender, EventArgs e)
        {
            String nameFilter = textBoxNameFilter.Text;
            DateTime dateTimeFilter = dateTimePickerFilter.Value.Date;
            String typeFilter = comboBoxFilter.Text;

            RefreshFolderAndFile();
            // Crea una lista para filtrar los datos (vacia)
            SortableBindingList<FolderAndFile> filteredList = new SortableBindingList<FolderAndFile>();
            // Añadir los elementos de subdirectories a filtered list
            foreach(FolderAndFile folderOrFile in subdirectories)
            {
                filteredList.Add(folderOrFile);
            }

            // Si el elemento no coincide con el filtro, se elimina de la lista de filtrado
            foreach(FolderAndFile folderOrFile in subdirectories)
            {
                if (!folderOrFile.Filename.ToLower().Trim().Contains(nameFilter.ToLower().Trim()))
                {
                    filteredList.Remove(folderOrFile);
                }

                if (folderOrFile.DateModified > dateTimeFilter)
                {
                    filteredList.Remove(folderOrFile);
                }

                if (folderOrFile.Type.Equals("File") && typeFilter.Equals("Folder"))
                {
                    filteredList.Remove(folderOrFile);
                }

                if (folderOrFile.Type.Equals("Folder") && typeFilter.Equals("File"))
                {
                    filteredList.Remove(folderOrFile);
                }
            }
            subdirectories = filteredList;
            dataGridViewFiles.DataSource = subdirectories;
        }

        private void SaveAsJsonToolStripMenuItem_Click(object sender, EventArgs e)
        {
            SaveFileDialog saveFileDialog = new SaveFileDialog();
            saveFileDialog.Filter = "JSON|*.json";
            if(saveFileDialog.ShowDialog() == DialogResult.OK)
            {
                SaveAsJson(saveFileDialog.FileName);
            }
        }

        // https://learn.microsoft.com/en-us/dotnet/standard/serialization/system-text-json/how-to?pivots=dotnet-8-0
        private void SaveAsJson(string fileName)
        {
            String fileJson = JsonConvert.SerializeObject(subdirectories, Formatting.Indented);
            File.WriteAllText(fileName, fileJson);
        }

        private void LoadFromJsonToolStripMenuItem_Click(object sender, EventArgs e)
        {
            OpenFileDialog openFileDialog = new OpenFileDialog();
            openFileDialog.Filter = "JSON|*.json";
            if (openFileDialog.ShowDialog() == DialogResult.OK)
            {
                LoadFromJson(openFileDialog.FileName);
            }
        }

        private void LoadFromJson(string fileName)
        {
            // leer el archivo
            String fileJson = File.ReadAllText(fileName);
            // convertir en objeto
            
            List<FolderAndFile> list = JsonConvert.DeserializeObject<List<FolderAndFile>>(fileJson);
            foreach (FolderAndFile folderAndFile in list)
            {
                folderAndFile.ImgDelete = imageDelete;
                folderAndFile.ImgEdit = imageEdit;
            }

            jsonData = list;

            useJsonData = true;

            newToolStripMenuItem.Enabled = false;

            RefreshFolderAndFile();


        }
    }
}
