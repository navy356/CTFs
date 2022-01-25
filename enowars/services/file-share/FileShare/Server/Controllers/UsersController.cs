using FileShare.Server.Data;
using FileShare.Server.Models;
using FileShare.Shared;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace FileShare.Server.Controllers
{
    [Authorize]
    [Route("api/[controller]")]
    [ApiController]
    public class UsersController : ControllerBase
    {

        private readonly ApplicationDbContext _context;

        public UsersController(ApplicationDbContext context)
        {
            _context = context;
        }

        // GET: api/<UserController>
        [HttpGet]
        public async Task<IActionResult> Get()
        {
            var result = new List<ApplicationUserDTO>();
            var users = await _context.Users.OrderByDescending(u => u.CreatedDate).Take(10000).ToListAsync();

            foreach (var applicationUser in users)
            {
                var tmp = new ApplicationUserDTO
                {
                    Id = applicationUser.Id,
                    Email = applicationUser.Email,
                    UserName = applicationUser.UserName,
                };

                result.Add(tmp);
            }

            return Ok(result);
        }
    }
}
