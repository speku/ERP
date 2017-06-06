using SAP.Middleware.Connector;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SAPTest
{
    class Connector
    {
        public class ECCDestinationConfig : IDestinationConfiguration
        {

            public bool ChangeEventsSupported()
            {
                return false;
            }

            public event RfcDestinationManager.ConfigurationChangeHandler ConfigurationChanged;

            public RfcConfigParameters GetParameters(string destinationName)
            {

                RfcConfigParameters parms = new RfcConfigParameters();

                if (destinationName.Equals("ABAP_AS"))
                {
                    parms.Add(RfcConfigParameters.AppServerHost, "/H/saprouter.hcc.in.tum.de/S/3299/H/131.159.9.153");
                    parms.Add(RfcConfigParameters.SystemNumber, "19");
                    parms.Add(RfcConfigParameters.User, "mroeck");
                    parms.Add(RfcConfigParameters.Password, "pelzbaum559");
                    parms.Add(RfcConfigParameters.Client, "904");
                    parms.Add(RfcConfigParameters.Language, "EN");
                }
                return parms;
            }
        }

        public RfcDestination Destination { get; }
        public Organization Organization { get; }

        public Connector()
        {
            ECCDestinationConfig cfg = new ECCDestinationConfig();
            RfcDestinationManager.RegisterDestinationConfiguration(cfg);
            Destination = RfcDestinationManager.GetDestination("ABAP_AS");
            Organization = new Organization(Destination);
            Destination.Ping();
            Console.WriteLine("connection established");
            Console.WriteLine(Organization);
        }

        public Customer Customer(string number) => new Customer(number, this);

    }
}
