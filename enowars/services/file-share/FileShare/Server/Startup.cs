using FileShare.Server.BackgroundServices;
using FileShare.Server.Data;
using FileShare.Server.Models;
using KK.DotNet.BackgroundTasks.Scheduled;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.HttpsPolicy;
using Microsoft.AspNetCore.ResponseCompression;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.FileProviders;
using Microsoft.Extensions.Hosting;
using System.IO;
using System.Linq;

namespace FileShare.Server
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        // For more information on how to configure your application, visit https://go.microsoft.com/fwlink/?LinkID=398940
        public void ConfigureServices(IServiceCollection services)
        {
            services.AddDbContext<ApplicationDbContext>(options =>
                options.UseNpgsql(
                    Configuration.GetConnectionString("DefaultConnection")));

            services.AddDatabaseDeveloperPageExceptionFilter();

            services.AddDefaultIdentity<ApplicationUser>(options =>
            {
                options.SignIn.RequireConfirmedAccount = false;
            })
                .AddEntityFrameworkStores<ApplicationDbContext>();

            services.AddIdentityServer()
                .AddInMemoryCaching()
                .AddApiAuthorization<ApplicationUser, ApplicationDbContext>();

            services.AddAuthentication()
                .AddIdentityServerJwt();

            services.AddScheduledTask<CleanupTask>(options =>
                {
                    options.Schedule = "*/1 * * * *";
                    options.CronFormat = Cronos.CronFormat.Standard;
                }
            );
            services.AddHostedService<SchedulerHostedService>();

            services.AddControllersWithViews();
            services.AddRazorPages();
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env, ApplicationDbContext dataContext)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
                app.UseMigrationsEndPoint();
                app.UseWebAssemblyDebugging();
            }
            else
            {
                app.UseExceptionHandler("/Error");
                // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
                //app.UseHsts();
            }

            dataContext.Database.Migrate();
            // Make Authentication working for http Requests
            app.UseCookiePolicy(new CookiePolicyOptions
            {
                MinimumSameSitePolicy = SameSiteMode.Lax,
            });
            // Chrome is certainly becoming the next IE11 Diva
            app.Use(async (ctx, next) =>
            {
                // 
                // Fuck Chrome! https://stackoverflow.com/a/51067035/9277073
                ctx.Response.Headers.Add("Content-Security-Policy",
                    "default-src 'self'; script-src 'unsafe-eval' 'unsafe-inline'; script-src-elem 'unsafe-eval' 'unsafe-inline' *; worker-src 'unsafe-eval' 'unsafe-inline' *; img-src 'self' data:;"
                    );
                await next();
            });
            app.UseBlazorFrameworkFiles();
            app.UseStaticFiles();

            // app.UseStaticFiles(new StaticFileOptions
            // {
            //     FileProvider = new PhysicalFileProvider(
            //     Path.Combine(env.ContentRootPath, "images")),
            //     RequestPath = "/userimages"
            // });

            app.UseRouting();
            app.UseIdentityServer();
            app.UseAuthentication();
            app.UseAuthorization();

            app.UseEndpoints(endpoints =>
            {
                endpoints.MapRazorPages();
                endpoints.MapControllers();
                endpoints.MapControllerRoute(
                    name: "fileshare",
                    pattern: "api/{controller=FileShare}/{*path}");
                endpoints.MapFallbackToFile("index.html");
            });
        }
    }
}
