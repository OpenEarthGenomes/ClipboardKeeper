# 📋 Clipboard Keeper (Vágólap Őr)

Egy egyszerű, de hatékony Android alkalmazás, amely a háttérben futva automatikusan elmenti a vágólapra másolt szövegeket egy helyi adatbázisba. Soha többé nem veszítesz el egy fontos linket vagy jegyzetet csak azért, mert véletlenül mást másoltál rá!

---

## 🚀 Funkciók

* **Automatikus mentés:** Minden másolt szöveg azonnal bekerül az adatbázisba.
* **Háttérfolyamat:** Az alkalmazás lezárt képernyőnél vagy más appok használata közben is aktív marad.
* **Adatbiztonság:** 100% offline működés. Nincs internet-hozzáférés, az adatok csak a te telefonodon tárolódnak.
* **Gyors törlés:** Lehetőség van egyes elemek vagy a teljes előzmény törlésére.
* **Indításkor indul:** A telefon újraindítása után automatikusan újraaktiválja magát.

---

## 🛠️ Technikai részletek

* **Nyelv:** Kotlin
* **UI:** Material Design 3 (ViewBinding)
* **Adattárolás:** Room Database (SQLite)
* **Háttérkezelés:** Foreground Service + BootReceiver
* **Target SDK:** Android 15 (API 36)

---

## 🏗️ Fordítás és Telepítés

A projekt automatizált **GitHub Actions** munkafolyamattal rendelkezik. Minden feltöltés (push) után az APK automatikusan elkészül.

1.  Menj az **Actions** fülre a GitHub-on.
2.  Válaszd ki a legutóbbi sikeres futtatást.
3.  Az **Artifacts** szekcióban töltsd le a `clipboard-keeper-final` fájlt.
4.  Csomagold ki és telepítsd az APK-t a telefonodra.

---

## 🛡️ Adatvédelem

Az alkalmazás **nem kér internet-jogosultságot**, így fizikailag képtelen adatokat továbbítani. Minden információ a készülék belső, védett tárhelyén marad.

> **Megjegyzés:** Mivel az app minden másolást rögzít, javasolt az érzékeny adatok (pl. jelszavak) manuális törlése az alkalmazás előzményeiből.

