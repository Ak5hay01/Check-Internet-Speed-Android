# Check-Internet-Speed-Android

We can find the speed by downloading a small image and check the start time and end time of the download. We can divide the (endTime - startTime) by 1000 to get time in seconds. Perform some Mathematical operations and you will get the speed of internet. We can check if mobile device connected to internet or not but its very difficult/tricky to find internet speed. You can easily find if device connected to intenet or not by default Android's Conneectivity Manager but it doesn't work each and every time. If device is connected to Router or mobile network(very remote areas where there is issue with network) but there is no internet then Android Connectivity manager will show the device is connected to internet even though there is no internet. If this situation is not handled your Application may not work correctly or Application may crash. 

# Solution

To handle this kind of tricky situations you can use a simple approach by checking if the device is connected or not by help of Android's Conneectivity Manager. Next write a funtion to download a image from network for checking if actually internet is working or not. If the file is downloaded then device is connected to internet and if not then its connected to Wifi/ mobile Network but there is no Internet. Speed is calculated in kilobytePerSec



# 1. If device is connected to internet and image is downloaded 

![alt text](/attachments/connected.png)



# 2. If device is connected to internet but image is not downloaded

![alt text](/attachments/connected_but_no_connection.png)


and last scenario


# 3. If device is not at all connected to internet

![alt text](/attachments/not_connected.png)
