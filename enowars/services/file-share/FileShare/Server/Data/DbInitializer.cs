using FileShare.Server.Models;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Identity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace FileShare.Server.Data
{
    public class DbInitializer
    {
        public static void Seed(ApplicationDbContext context)
        {
#if DEBUG
            if (!context.Users.Any())
            {
                var admin = new ApplicationUser
                {
                    Id = "9daaf70c-7e01-49e3-8c54-072965b2b638",
                    Email = "test@example.de",
                    UserName = "admin"
                };
                admin.PasswordHash = new PasswordHasher<ApplicationUser>().HashPassword(admin, "secret");

                context.Users.Add(admin);
            }

            context.SaveChanges();
#endif
        }

    }
}
