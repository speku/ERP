using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SAP.Middleware.Connector;

namespace SAPTest
{
    class Customer
    {
        public List<Invoice> Invoices { get; set; }

        public Connector Connector { get; set; }
        public Organization Organization {get; set;}

        public String
        number,
        firstName,
        lastName,
        city,
        district,
        postBox,
        postalCode,
        countryCode,
        cityCode,
        street,
        telephoneNumber,
        postBoxPostalCode;

        public Customer(String num, Connector connector)
        {
            number = num.PadLeft(10, '0');
            Connector = connector;
            Organization = connector.Organization;
            GetCustomerDetails();
            Invoices = GetSalesOrders(number).Select(x => new Invoice(this, x)).ToList();
        }

        public void Check()
        {
            Console.WriteLine(ToString());
            Invoices.ForEach(i => Console.WriteLine($"{i.ToString()}\n---------------------------------"));
        }


        public List<Dictionary<String, String>> GetOrderDetails(String invoiceNumber)
        {
            try
            {
                invoiceNumber = invoiceNumber.PadLeft(10, '0');
                var f = Function("BAPI_BILLINGDOC_GETLIST");
                var struc = f.GetStructure("REFDOCRANGE");
                struc.SetValue("SIGN", "I");
                struc.SetValue("REF_DOC_LOW", invoiceNumber);
                struc.SetValue("REF_DOC_HIGH", invoiceNumber);
                struc.SetValue("OPTION", "EQ");
                f.Invoke(Connector.Destination);
                Result(f);
                var table = f.GetTable("BILLINGDOCUMENTDETAIL");
                return Util.Table(table);
            }
            catch (Exception _) {
                return new List<Dictionary<string, string>>();
            }
        }

        IRfcFunction Function(string f) => Connector.Destination.Repository.CreateFunction(f);

        static void Result(IRfcFunction f)
        {
            Console.WriteLine(f.GetStructure("RETURN").GetString("TYPE"));
            Console.WriteLine(f.GetStructure("RETURN").GetString("MESSAGE"));
        }

        public void GetCustomerDetails()
        {
            try
            {
                var f = Function("BAPI_CUSTOMER_GETDETAIL2");
                f.SetValue("CUSTOMERNO", number);
                f.Invoke(Connector.Destination);
                var info = f.GetStructure("CUSTOMERADDRESS");
                Result(f);
                firstName = info.GetString("NAME");
                lastName = info.GetString("NAME_2");
                city = info.GetString("CITY");
                district = info.GetString("DISTRICT");
                postBox = info.GetString("PO_BOX");
                postBoxPostalCode = info.GetString("POBX_PCD");
                postalCode = info.GetString("POSTL_CODE");
                countryCode = info.GetString("COUNTYCODE");
                cityCode = info.GetString("CITY_CODE");
                street = info.GetString("STREET");
                telephoneNumber = info.GetString("TELEPHONE");
            }
            catch (Exception _) {  }
        }

        private List<Dictionary<String, String>> GetSalesOrders(String customerNumber)
        {

            try
            {
                var f = Function("BAPI_SALESORDER_GETLIST");
                f.SetValue("CUSTOMER_NUMBER", customerNumber); 
                f.SetValue("SALES_ORGANIZATION", "1000");
                f.Invoke(Connector.Destination);
                Result(f);
                return Util.Table(f.GetTable("SALES_ORDERS"));

            }
            catch (Exception e)
            {
                return new List<Dictionary<string, string>>();
            }
        }

        public override String ToString()
        {
            return $"{firstName}\n{lastName}\n{street}\n{city}";
        }

    }
}
