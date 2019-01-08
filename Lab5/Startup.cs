using Microsoft.Owin;
using Owin;

[assembly: OwinStartupAttribute(typeof(_17272381_OnlineStore.Startup))]
namespace _17272381_OnlineStore
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
        }
    }
}
