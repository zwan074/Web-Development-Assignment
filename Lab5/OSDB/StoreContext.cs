using _17272381_OnlineStore.Models;
using System.Data.Entity;


namespace _17272381_OnlineStore.OSDB
{
    public class StoreContext:DbContext
    {
        public DbSet<Product> Products { get; set; }
        public DbSet<Category> Categories { get; set; }
    }
}