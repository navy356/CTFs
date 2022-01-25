using FileShare.Server.Data;
using FileShare.Server.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Authorization;
using Microsoft.AspNetCore.Mvc.Filters;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Primitives;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Security.Claims;
using System.Threading.Tasks;

namespace FileShare.Server.Filters
{
    public class CustomFileShareAuthorizationAttribute : Attribute, IAuthorizationFilter
    {
        // Cache authorized Users
        private MemoryCacheWithPolicy<bool> _cache = new MemoryCacheWithPolicy<bool>();
        public CustomFileShareAuthorizationAttribute() { }

        public void OnAuthorization(AuthorizationFilterContext context)
        {
            var dbcontext = context.HttpContext.RequestServices.GetRequiredService<ApplicationDbContext>();
            var currentUser = context.HttpContext.User.FindFirstValue(ClaimTypes.NameIdentifier);

            StringValues sharedUserQuery;
            context.HttpContext.Request.Query.TryGetValue("sharedUser", out sharedUserQuery);
            var sharedUserId = sharedUserQuery.FirstOrDefault();
            if (sharedUserId == null)
            {
                context.Result = new JsonResult("Parameter 'sharedUser' not supplied.") { StatusCode = (int)HttpStatusCode.BadRequest };
                return;
            }
            var isAuthorized = false;
            try
            {
                isAuthorized = _cache.GetOrCreate(currentUser, () =>
            {
                var sharedUser = (from c in dbcontext.Users where c.Id == sharedUserId select c).FirstOrDefault();
                if (sharedUser == null)
                {
                    throw new SharedUserNotExistentException();
                }

                if (sharedUser.SharedWithUsers == null || !sharedUser.SharedWithUsers.Any(item => item == currentUser))
                {
                    return false;
                }
                return true;
            });

            }
            catch (SharedUserNotExistentException)
            {
                context.Result = new JsonResult("Shared User not existent.") { StatusCode = (int)HttpStatusCode.BadRequest };
                return;
            }


            if (!isAuthorized)
            {
                context.Result = new JsonResult("Not Authorized") { StatusCode = (int)HttpStatusCode.Unauthorized };
                return;
            }
        }
    }

    public class SharedUserNotExistentException : Exception
    {

    }
}
