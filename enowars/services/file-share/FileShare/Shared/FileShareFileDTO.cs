using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FileShare.Shared
{
    public class FileShareFileDTO
    {
        public string Id{ get; set; }
        public string Name { get; set; }
        public long Size{ get; set; }
        public DateTime Date{ get; set; }
    }
}
