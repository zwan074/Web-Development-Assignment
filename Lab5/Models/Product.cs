using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace _17272381_OnlineStore.Models
{
    public partial class Product 
    {
        
        public int ID { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public decimal Price { get; set; }
        public int? CategoryID { get; set; }
        public virtual Category Category { get; set; }
    }

    public partial class Category
    {
        [Key]
        public int ID { get; set; }
        public string Name { get; set; }
        public virtual ICollection<Product> Products { get; set; }
    }
}