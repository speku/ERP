using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SAP.Middleware.Connector;

namespace SAPTest
{
    static class Util
    {
        public static List<Dictionary<String, String>> Table(IRfcTable table)
        {
            var result = new List<Dictionary<String, String>>();

            foreach (var row in table)
            {
                var rowMap = new Dictionary<String, String>();
                result.Add(rowMap);
                foreach (var column in row)
                {
                    rowMap.Add(column.Metadata.Name, column.GetString());
                }
            }
            return result;
        }
    }
}
