using Microsoft.AspNetCore.Identity;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace FileShare.Server.Models
{
    public class ApplicationUser : IdentityUser
    {
        public List<string> SharedWithUsers { get; set; }
        public List<string> group { get; set; }
        public DateTime CreatedDate { get; set; }
    }
}
