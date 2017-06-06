using SAP.Middleware.Connector;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ERP_WebInterface
{
    public class Organization
    {
        private RfcDestination destination;

        public String
        organization,
        division,
        group,
        office;

        public Organization(RfcDestination destination)
        {
            this.destination = destination;
            GetOrganizationDetails();
        }

        public void GetOrganizationDetails()
        {
            try
            {
                var f = destination.Repository.CreateFunction("BAPI_SALESORG_GET_DETAIL");
                f.SetValue("SALESORGANIZATION", "1000");
                f.Invoke(destination);
                var info = f.GetStructure("SDORGDATA");
                Console.WriteLine(f.GetStructure("RETURN").GetString("TYPE"));
                Console.WriteLine(f.GetStructure("RETURN").GetString("MESSAGE"));
                organization = info.GetString("SALESORG_TEXT") ?? "";
                division = info.GetString("DIVISION_TEXT") ?? "";
                group = info.GetString("SALES_GRP_TEXT") ?? "";
                office = info.GetString("SALES_OFF_TEXT") ?? "";
            }
            catch (Exception e) { }
        }

        public override String ToString()
        {
            return $"{organization}\n{division}\n{group}\n{office}";
        }
    }
}
