# JRTCP
## RTCP Module

### Reference
https://www4.cs.fau.de/Projects/JRTP/pmt/node82.html
  
https://www.freesoft.org/CIE/RFC/1889/13.htm
  
https://datatracker.ietf.org/doc/html/rfc1889
  
### Example
#### Single packet
  
~~~
11:57:34.603 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][srCreationTest] RtcpPacket: 
{
  "rtcpHeader": {
    "version": 2,
    "padding": 0,
    "paddingBytes": 0,
    "resourceCount": 1,
    "packetType": 200,
    "length": 12,
    "ssrc": 26422708
  },
  "rtcpFormat": {
    "mswNts": 933147501,
    "lswNts": 933147500,
    "rts": 250880,
    "spc": 1568,
    "soc": 2508,
    "rtcpReportBlockList": [
      {
        "ssrc": 1569920308,
        "f": 0,
        "c": 1,
        "ehsn": 50943,
        "j": 76,
        "lsr": 933147500,
        "dlsr": 35390
      }
    ]
  }
}
11:57:34.606 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][srCreationTest] RtcpPacketTest byte data: (size=52)
[-127, -56, 0, 12, 1, -109, 45, -76, 55, -98, -77, 109, 55, -98, -77, 108, 0, 3, -44, 0, 0, 0, 6, 32, 0, 0, 9, -52, 93, -109, 21, 52, 0, 0, 0, 1, 0, 0, -58, -1, 0, 0, 0, 76, 55, -98, -77, 108, 0, 0, -118, 62]
11:57:34.609 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][srCreationTest] RtcpPacketTest byte data: 
81 c8 00 0c 01 93 2d b4 37 9e b3 6d 37 9e b3 6c 00 03 d4 00 00 00 06 20 00 00 09 cc 5d 93 15 34 00 00 00 01 00 00 c6 ff 00 00 00 4c 37 9e b3 6c 00 00 8a 3e 
11:57:34.622 [main] DEBUG instance.BaseEnvironment - Success to send the data. (size=52)

11:57:34.634 [nioEventLoopGroup-3-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] remainDataLength: [52], index: [0]
11:57:34.634 [nioEventLoopGroup-3-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] RtcpHeader[8]: 
[{
  "version": 2,
  "padding": 0,
  "paddingBytes": 0,
  "resourceCount": 1,
  "packetType": 200,
  "length": 12,
  "ssrc": 26422708
}]
index: [8]
11:57:34.635 [nioEventLoopGroup-3-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] totalDataLength: [52], index: [8], PacketLength: [12], curRemainDataLength: [44]
11:57:34.636 [nioEventLoopGroup-3-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] RtcpPacket: (size=8+44)
{
  "rtcpHeader": {
    "version": 2,
    "padding": 0,
    "paddingBytes": 0,
    "resourceCount": 1,
    "packetType": 200,
    "length": 12,
    "ssrc": 26422708
  },
  "rtcpFormat": {
    "mswNts": 933147501,
    "lswNts": 933147500,
    "rts": 250880,
    "spc": 1568,
    "soc": 2508,
    "rtcpReportBlockList": [
      {
        "ssrc": 1569920308,
        "f": 0,
        "c": 1,
        "ehsn": 50943,
        "j": 76,
        "lsr": 933147500,
        "dlsr": 35390
      }
    ]
  }
}
11:57:34.640 [nioEventLoopGroup-3-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] remainDataLength: [0], index: [52]
~~~
  
#### Compound packet

  
