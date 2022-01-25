using FileShare.Server.Data;
using FileShare.Server.Filters;
using FileShare.Shared;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Primitives;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Security.Claims;
using System.Threading.Tasks;

namespace FileShare.Server.Controllers
{

    [Authorize]
    [ApiController]
    [Route("api/[controller]")]
    public class FileShareController : Controller
    {
        public static string path = Directory.GetCurrentDirectory() + "/files";
        public static string convertpath = Directory.GetCurrentDirectory() + "/files/convert";
        private readonly ApplicationDbContext _dbcontext;
        private int quota = 20;


        private readonly ILogger<FileShareController> _logger;

        public FileShareController(ILogger<FileShareController> logger, ApplicationDbContext dbcontext)
        {
            _logger = logger;
            _dbcontext = dbcontext;
        }


        [HttpPost("convert/{fileName}")]
        [RequestSizeLimit(2147483648)]
        public async Task<IActionResult> UploadPicture(string fileName, IFormFile file)
        {
            if (file == null || file.Length == 0)
            {
                return BadRequest("No Payload.");
            }
            var userId = User.FindFirstValue(ClaimTypes.NameIdentifier);
            var filepath = Path.Combine(convertpath, userId, fileName);
            var outFilepath = Path.Combine(path, userId, fileName);
            if (Directory.Exists(Path.GetDirectoryName(outFilepath)))
            {
                string[] fileEntries = Directory.GetFiles(Path.GetDirectoryName(outFilepath));
                if (fileEntries.Length > this.quota)
                {
                    return BadRequest("Quota exceeded.");
                }
            }
            try
            {
                if (!Directory.Exists(Path.GetDirectoryName(filepath)))
                {
                    Directory.CreateDirectory(Path.GetDirectoryName(filepath));
                }
                using (var stream = new FileStream(filepath, FileMode.OpenOrCreate))
                {
                    await file.CopyToAsync(stream);
                }
            }
            catch (Exception err)
            {
                this._logger.LogError(err, err.Message);
                return StatusCode(500, "Internal Server Error");
            };

            return Ok();
        }

        [HttpPost("{*fileName}")]
        [RequestSizeLimit(2147483648)]
        public async Task<IActionResult> UploadFile([FromRoute] string fileName, IFormFile file)
        {
            if (file == null || file.Length == 0)
            {
                return BadRequest("No Payload.");
            }
            var userId = User.FindFirstValue(ClaimTypes.NameIdentifier);
            var filepath = Path.Combine(path, userId, fileName);
            try
            {
                if (!Directory.Exists(Path.GetDirectoryName(filepath)))
                {
                    Directory.CreateDirectory(Path.GetDirectoryName(filepath));
                }
                string[] fileEntries = Directory.GetFiles(Path.GetDirectoryName(filepath));
                if (fileEntries.Length > this.quota)
                {
                    return BadRequest("Quota exceeded.");
                }
                using (var stream = new FileStream(filepath, FileMode.OpenOrCreate))
                {
                    await file.CopyToAsync(stream);
                }
            }
            catch (Exception err)
            {
                this._logger.LogError(err, err.Message);
                return StatusCode(500, "Internal Server Error");
            };

            return Ok();
        }

        [HttpDelete("{*fileName}")]
        public IActionResult DeletePicture(string fileName)
        {
            var userId = User.FindFirstValue(ClaimTypes.NameIdentifier);

            var filepath = Path.Combine(path, userId, fileName);

            System.IO.File.Delete(filepath);

            return Ok();
        }

        [HttpGet()]
        public async Task<IActionResult> GetFileList()
        {
            var userId = User.FindFirstValue(ClaimTypes.NameIdentifier);

            var filepath = Path.Combine(path, userId);

            var fileList = new List<FileShareFileDTO>();

            if (Directory.Exists(filepath))
            {
                DirectoryInfo di = new DirectoryInfo(filepath);
                foreach (FileInfo file in di.GetFiles())
                    fileList.Add(new FileShareFileDTO()
                    {
                        Id = file.Name,
                        Name = file.Name,
                        Date = file.CreationTimeUtc,
                        Size = file.Length
                    });
            }
            return Ok(fileList);
        }

        [HttpGet("{*fileName}")]
        public async Task<IActionResult> GetFile(string fileName)
        {
            var userId = User.FindFirstValue(ClaimTypes.NameIdentifier);

            var filepath = Path.Combine(path, userId, fileName);
            try
            {
                FileContentResult result = new FileContentResult(System.IO.File.ReadAllBytes(filepath), "application/octet-stream")
                {
                    FileDownloadName = fileName
                };
                return result;
            }
            catch (FileNotFoundException)
            {
                return NotFound();
            }
        }


        [HttpGet("shared")]
        [CustomFileShareAuthorization()]
        public async Task<IActionResult> GetSharedFileList()
        {
            StringValues sharedUserQuery;

            HttpContext.Request.Query.TryGetValue("sharedUser", out sharedUserQuery);
            var sharedUserId = sharedUserQuery.FirstOrDefault();
            if (sharedUserId == null)
            {
                return new JsonResult("Parameter 'sharedUser' not supplied.") { StatusCode = (int)HttpStatusCode.BadRequest };

            }
            var sharedUser = (from c in _dbcontext.Users where c.Id == sharedUserId select c).FirstOrDefault();
            if (sharedUser == null)
            {
                return new JsonResult("Shared User not existent.") { StatusCode = (int)HttpStatusCode.BadRequest };
            }
            var filepath = Path.Combine(path, sharedUserId);

            var fileList = new List<FileShareFileDTO>();

            if (Directory.Exists(filepath))
            {
                DirectoryInfo di = new DirectoryInfo(filepath);
                foreach (FileInfo file in di.GetFiles())
                    fileList.Add(new FileShareFileDTO()
                    {
                        Id = file.Name,
                        Name = file.Name,
                        Date = file.CreationTimeUtc,
                        Size = file.Length
                    });
            }
            return Ok(fileList);
        }

        [HttpGet("shared/{*fileName}")]
        [CustomFileShareAuthorization()]
        public async Task<IActionResult> GetSharedFile(string fileName)
        {
            StringValues sharedUserQuery;

            HttpContext.Request.Query.TryGetValue("sharedUser", out sharedUserQuery);
            var sharedUserId = sharedUserQuery.FirstOrDefault();
            if (sharedUserId == null)
            {
                return new JsonResult("Parameter 'sharedUser' not supplied.") { StatusCode = (int)HttpStatusCode.BadRequest };

            }
            var sharedUser = (from c in _dbcontext.Users where c.Id == sharedUserId select c).FirstOrDefault();
            if (sharedUser == null)
            {
                return new JsonResult("Shared User not existent.") { StatusCode = (int)HttpStatusCode.BadRequest };
            }

            var filepath = Path.Combine(path, sharedUserId, fileName);
            if (System.IO.File.Exists(filepath))
            {
                FileContentResult result = new FileContentResult(System.IO.File.ReadAllBytes(filepath), "application/octet-stream")
                {
                    FileDownloadName = fileName
                };
                return result;
            }
            return NotFound();

        }
    }
}
