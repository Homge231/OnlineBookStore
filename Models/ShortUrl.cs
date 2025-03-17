using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;

namespace URLShortener.Models
{
    public class ShortUrl
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; } = string.Empty; // Khởi tạo giá trị mặc định

        [BsonElement("fullUrl")]
        public string FullUrl { get; set; } = string.Empty; // Khởi tạo giá trị mặc định

        [BsonElement("shortCode")]
        public string ShortCode { get; set; } = string.Empty; // Khởi tạo giá trị mặc định

        [BsonElement("clicks")]
        public int Clicks { get; set; } = 0;

        [BsonElement("createdAt")]
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;

        // Constructor khởi tạo giá trị mặc định
        public ShortUrl(string fullUrl, string shortCode)
        {
            FullUrl = fullUrl ?? throw new ArgumentNullException(nameof(fullUrl));
            ShortCode = shortCode ?? throw new ArgumentNullException(nameof(shortCode));
        }

        // Constructor mặc định (nếu cần)
        public ShortUrl() { }
    }
}
