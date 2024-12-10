import unittest
from sqlalchemy.orm import Session
import sys
import os
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '../')))
from config.db import get_db
from routes.userRoutes import get_user_by_id_endpoint, all_users_role_id, get_monitor_by_id_endpoint

class ConsultarMonitoresTest(unittest.TestCase):

    def setUp(self):
        """Prepara una sesión de base de datos y datos de prueba."""
        self.db: Session = next(get_db())
    
    def test_CM_01(self):
        """Consultar un monitor que existe en la base de datos, comprobando con nombre y telefono"""
        response = get_monitor_by_id_endpoint(3, self.db)
        self.assertEqual([response.name, response.role], ["Dany", "Monitor"])
    
    def test_CM_02(self):
        """Consultar un usuario que no es un monitor"""
        response = get_monitor_by_id_endpoint(1, self.db)
        self.assertEqual(response, {"error": "User not monitor"})
    
    def test_CM_03(self):
        """Consultar un monitor ingresando un dato distinto a un codigo"""
        response = get_monitor_by_id_endpoint("Bryan", self.db)
        self.assertEqual(response, {"error": "Dato ingresado no es valido."})
    
    def test_CM_04(self):
        """Consultar todos los monitores existentes actualmente"""
        response = all_users_role_id(2, self.db)
        print(response)
        self.assertEqual([monitor["name"] for monitor in response], ["Dany", "Jonas"])
    
    def test_CM_05(self):
        """Consultar un monitor no existente en la base de datos"""
        response = get_monitor_by_id_endpoint(99, self.db)
        self.assertEqual(response, {"error": "User not found"})



if __name__ == '__main__':
    unittest.main()