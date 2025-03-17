using Microsoft.AspNetCore.Mvc;
using URLShortener.Models;
using URLShortener.Services;
using System.Threading.Tasks;

namespace URLShortener.Controllers
{
    public class ShortUrlWebController : Controller
    {
        private readonly ShortUrlService _shortUrlService;

        public ShortUrlWebController(ShortUrlService shortUrlService)
        {
            _shortUrlService = shortUrlService;
        }

        public IActionResult Index()
        {
            return View();
        }

        [HttpPost("ShortUrl/Shorten")]
        public async Task<IActionResult> Shorten(string fullUrl)
        {
            if (string.IsNullOrEmpty(fullUrl))
                return View("Index");

            var shortCode = _shortUrlService.GenerateShortCode();
            var newShortUrl = new ShortUrl
            {
                FullUrl = fullUrl,
                ShortCode = shortCode
            };

            await _shortUrlService.CreateAsync(newShortUrl);

            ViewBag.ShortenedUrl = $"{Request.Scheme}://{Request.Host}/{shortCode}";
            return View("Index");
        }

        [HttpGet("{shortCode}")]
        public async Task<IActionResult> RedirectToUrl(string shortCode)
        {
            var shortUrl = await _shortUrlService.GetByShortCodeAsync(shortCode);
            if (shortUrl == null)
            {
                return NotFound("Shortened URL not found.");
            }

            shortUrl.Clicks++;
            await _shortUrlService.UpdateClicksAsync(shortUrl.Id, shortUrl.Clicks);

            return Redirect(shortUrl.FullUrl);
        }
    }
}
