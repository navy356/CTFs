using FileShare.Server.Controllers;
using FileShare.Server.Models;
using IdentityServer4.EntityFramework.Options;
using Microsoft.AspNetCore.ApiAuthorization.IdentityServer;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;

namespace FileShare.Server.Data
{
    public class ApplicationDbContext : ApiAuthorizationDbContext<ApplicationUser>
    {
        private readonly ILogger<ApplicationDbContext> _logger;

        public ApplicationDbContext(
            DbContextOptions options,
            IOptions<OperationalStoreOptions> operationalStoreOptions, ILogger<ApplicationDbContext> logger
            ) : base(options, operationalStoreOptions)
        {
            _logger = logger;
        }

        public void CustomApplicationUserCreatedHook()
        {
            var entries = this.ChangeTracker.Entries().Where(e => e.State == EntityState.Added).Where(e => {
                var typ = e.Entity.GetType();
                return typeof(ApplicationUser).IsAssignableFrom(typ);
            }).ToArray();
            foreach (var entry in entries)
            {
                // Ensre CreatedDate is set
                ((ApplicationUser)entry.Entity).CreatedDate = DateTime.Now;

                // Ensure Data and Convert Folders are created
                var id = (entry.Entity as ApplicationUser).Id;
                try
                {
                    var convertfilepath = Path.Combine(FileShareController.convertpath, id);
                    if (!Directory.Exists(convertfilepath))
                    {
                        Directory.CreateDirectory(convertfilepath);
                    }
                    var filepath = Path.Combine(FileShareController.path, id);
                    if (!Directory.Exists(filepath))
                    {
                        Directory.CreateDirectory(filepath);
                    }
                }
                catch (Exception err)
                {
                    // Ignore it
                    this._logger.LogError(err, err.Message);
                };
            }
        }

        public override int SaveChanges(bool acceptAllChangesOnSuccess)
        {
            CustomApplicationUserCreatedHook();
            return base.SaveChanges(acceptAllChangesOnSuccess);
        }

        public override Task<int> SaveChangesAsync(bool acceptAllChangesOnSuccess, CancellationToken cancellationToken = default)
        {
            CustomApplicationUserCreatedHook();
            return base.SaveChangesAsync(acceptAllChangesOnSuccess, cancellationToken);
        }

    }
}
