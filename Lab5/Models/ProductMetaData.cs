using System.ComponentModel.DataAnnotations;


namespace _17272381_OnlineStore.Models
{
    [MetadataType(typeof(ProductMetaData))]
    public partial class Product
    {
    }
    public class ProductMetaData
    {
        [Display(Name = "Product Name")]
        public string Name;
    }
}