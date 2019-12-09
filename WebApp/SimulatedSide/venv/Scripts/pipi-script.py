#!"C:\Users\abar\Desktop\Cours CPE\4A\Projet Transversal\Projet_Transversal\WebApp\SimulatedSide\venv\Scripts\python.exe"
# EASY-INSTALL-ENTRY-SCRIPT: 'pipi==1.0.1','console_scripts','pipi'
__requires__ = 'pipi==1.0.1'
import re
import sys
from pkg_resources import load_entry_point

if __name__ == '__main__':
    sys.argv[0] = re.sub(r'(-script\.pyw?|\.exe)?$', '', sys.argv[0])
    sys.exit(
        load_entry_point('pipi==1.0.1', 'console_scripts', 'pipi')()
    )
