# LeitorUnificadoPoc
ML Kit &amp; CameraX Reader POC

## Funcionalities description
- Barcode and QR Code reader

## Tech stack
- Minimum SDK level 21
- [Kotlin](https://kotlinlang.org/) based + [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) for asynchronous.
- JetPack
  - LiveData - notify domain layer data to views.
  - Lifecycle - dispose of observing data when lifecycle state changes.
  - ViewModel - UI related data holder, lifecycle aware.
  - ML Kit & CameraX Libs

## Functionalities demonstration
<img src="https://github.com/leogmsantos/LeitorUnificadoPoc/blob/main/demonstration.gif" width="280" height="400"/>

## How to integrate
- Clone the project 
- Call the BarcodeScannerManager.Builder() who will return an BarcodeScannerApplication instance
- Use BarcdeScannerApplication.startBarcodeScanner() to init the application
- You will need implement some parameters to BarcodeScannerManager.Builder(), one of them is PreviewView who should be in your View. 

## APK Download
Download the APK in the following link to test in your Android Phone: 
https://drive.google.com/file/d/1kdbpGEDHOXShQWXTABN3q112I8GfTHI0/view?usp=sharing
