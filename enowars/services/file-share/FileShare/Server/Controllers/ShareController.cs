using FileShare.Server.Data;
using FileShare.Server.Models;
using FileShare.Shared;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Security.Claims;
using System.Threading.Tasks;

namespace FileShare.Server.Controllers
{
    [Authorize]
    [Route("api/[controller]")]
    [ApiController]
    public class ShareController : ControllerBase
    {

        private readonly ApplicationDbContext _context;
        private readonly UserManager<ApplicationUser> _userManager;

        public ShareController(ApplicationDbContext context, UserManager<ApplicationUser> userManager)
        {
            _context = context;
            _userManager = userManager;
        }

        [HttpGet]
        public async Task<IActionResult> Get()
        {
            var userId = User.FindFirstValue(ClaimTypes.NameIdentifier);

            var currentUser = _context.Users.First(a => a.Id == userId);

            return Ok(currentUser.SharedWithUsers ?? new List<string>());
        }

        [HttpGet("withme")]
        public async Task<IActionResult> GetSharedWithMe()
        {
            var userId = User.FindFirstValue(ClaimTypes.NameIdentifier);
            var sharedWithMe = _context.Users.Where(user => user.SharedWithUsers.Contains(userId));

            return Ok(sharedWithMe.Select(user => user.Id).ToArray());
        }

        [HttpPost]
        public async Task<IActionResult> Post(List<string> ids)
        {
            var userId = User.FindFirstValue(ClaimTypes.NameIdentifier);

            foreach (var id in ids)
            {
                try
                {
                    var sharedId = _context.Users.First(a => a.Id == id);
                    if (sharedId == null)
                    {
                        return new JsonResult("Shared User not existent (" + id + ").") { StatusCode = (int)HttpStatusCode.BadRequest };
                    }
                    if (sharedId.Id == userId)
                    {
                        return new JsonResult("You can't share files with yourself.") { StatusCode = (int)HttpStatusCode.BadRequest };
                    }
                }
                catch (InvalidOperationException)
                {
                    return new JsonResult("Shared User not existent (" + id + ").") { StatusCode = (int)HttpStatusCode.BadRequest };
                }
            }


            var currentUser = _context.Users.First(a => a.Id == userId);
            currentUser.SharedWithUsers = ids;
            await _context.SaveChangesAsync();

            return Ok(currentUser.SharedWithUsers);
        }
    }
}
