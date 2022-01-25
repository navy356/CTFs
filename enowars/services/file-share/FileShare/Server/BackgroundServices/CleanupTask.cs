using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using System.Threading;
using KK.DotNet.BackgroundTasks.Scheduled;
using System.IO;
using FileShare.Server.Controllers;
using FileShare.Server.Data;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.AspNetCore.Identity;
using FileShare.Server.Models;

namespace FileShare.Server.BackgroundServices
{
    public class CleanupTask : IScheduledTask
    {
            private readonly int autoDeleteTimeFrame = 11; // In minutes
            private readonly ILogger<CleanupTask> logger;

        private readonly IServiceScopeFactory scopeFactory;

        public CleanupTask(
                IScheduledTaskOptions<CleanupTask> options,
                ILogger<CleanupTask> logger,
                IServiceScopeFactory scopeFactory
            )
            {
                this.Options = options;
                this.logger = logger;
                this.scopeFactory = scopeFactory;

        }

        public IScheduledTaskOptions<IScheduledTask> Options { get; }

        public async Task ExecuteAsync(CancellationToken stoppingToken)
        {
            using (var scope = scopeFactory.CreateScope())
            {
                this.logger.LogDebug("Started CleanUp Task");

                var userManager = scope.ServiceProvider.GetRequiredService<UserManager<ApplicationUser>>();

                // Get a list of all subdirectories  
                var dirs = from dir in
                Directory.EnumerateDirectories(FileShareController.path)
                           select dir;
                Console.WriteLine("Subdirectories: {0}", dirs.Count<string>().ToString());

                DateTime now = DateTime.Now;
                foreach (var dir in dirs)
                {
                    if (File.GetCreationTime(dir).AddMinutes(this.autoDeleteTimeFrame) < now)
                    {
                        // Delete user
                        string id = Path.GetFileName(dir);
                        var user = await userManager.FindByIdAsync(id);
                        if(user != null)
                        {

                            var logins = await userManager.GetLoginsAsync(user);
                            //var rolesForUser = await userManager.GetRolesAsync(user);
                        

                            foreach (var login in logins.ToList())
                            {
                                await userManager.RemoveLoginAsync(user, login.LoginProvider, login.ProviderKey);
                            }

                            //if (rolesForUser.Count() > 0)
                            //{
                            //    foreach (var item in rolesForUser.ToList())
                            //    {
                            //        // item should be the name of the role
                            //        var result = await userManager.RemoveFromRoleAsync(user, item);
                            //    }
                            //}

                            await userManager.DeleteAsync(user);
                        // Delete directory
                        Directory.Delete(dir, true);
                        string convertPath = Path.Combine(FileShareController.convertpath, id);
                        if (Directory.Exists(convertPath))
                            {
                                Directory.Delete(convertPath, true);

                            }
                            this.logger.LogDebug("Purged: " + dir);
                        }

                    }
                }
                this.logger.LogDebug("Finished CleanUp Task");
            }
        }
    }
}
