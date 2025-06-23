import sqlite3
import os

def scan_and_check():
    db_files = [f for f in os.listdir(".") if f.endswith(".db")]
    if not db_files:
        print(" No .db database files were found.")
        return

    for db_file in db_files:
        print(f"\n Checking the database file: {db_file}")
        try:
            conn = sqlite3.connect(db_file)
            cursor = conn.cursor()
            cursor.execute("SELECT * FROM objects")
            rows = cursor.fetchall()

            if not rows:
                print("（The database is empty and there are no object records.）")
            else:
                for row in rows:
                    name, address, language, version, region, last_seen = row
                    print(f"  🔹 Name: {name}")
                    print(f"     Address: {address}")
                    print(f"     Language: {language}, Version: {version}, Region: {region}")
                    print(f"     Last Seen (timestamp): {last_seen}")
        except Exception as e:
            print(f"⚠ Read {db_file} failure: {e}")
        finally:
            conn.close()

if __name__ == "__main__":
    scan_and_check()
