using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Directory_And_File_ForEver
{
    internal class FolderAndFile
    {

        public String Filename { get; set; }
        public DateTime DateModified { get; set; }
        public String Type { get; set; }
        public String Size { get; set; }

        [JsonIgnore]
        public Bitmap ImgEdit { get; set; }

        [JsonIgnore]
        public Bitmap ImgDelete { get; set; }

    }
}
