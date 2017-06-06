using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SAP.Middleware.Connector;

namespace SAPTest
{
    class Program
    {
        static void Main(string[] args)
        {
            var c = new Connector();
            //string[] customers = { "1390", "1350", "1000" };
            string[] customers = { "1390" };
            customers.ToList().ForEach(x => c.Customer(x).Check());
            Console.ReadKey();
        }
    }

}
