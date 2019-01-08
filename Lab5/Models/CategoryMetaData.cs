using System.ComponentModel.DataAnnotations;

namespace _17272381_OnlineStore.Models
{
    [MetadataType(typeof(CategoryMetaData))]
    public partial class Category
    {
    }

    public class CategoryMetaData
    {
        
        [Display(Name = "Category Name")]
        public string Name;

    }
}