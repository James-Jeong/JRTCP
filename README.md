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
  
~~~
11:57:35.712 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][srCreationTest] RtcpPacket: 
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
    "mswNts": 933148638,
    "lswNts": 933148638,
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
        "lsr": 933148638,
        "dlsr": 35390
      }
    ]
  }
}
11:57:35.717 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][srCreationTest] RtcpPacketTest byte data: (size=52)
[-127, -56, 0, 12, 1, -109, 45, -76, 55, -98, -73, -34, 55, -98, -73, -34, 0, 3, -44, 0, 0, 0, 6, 32, 0, 0, 9, -52, 93, -109, 21, 52, 0, 0, 0, 1, 0, 0, -58, -1, 0, 0, 0, 76, 55, -98, -73, -34, 0, 0, -118, 62]
11:57:35.720 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][srCreationTest] RtcpPacketTest byte data: 
81 c8 00 0c 01 93 2d b4 37 9e b7 de 37 9e b7 de 00 03 d4 00 00 00 06 20 00 00 09 cc 5d 93 15 34 00 00 00 01 00 00 c6 ff 00 00 00 4c 37 9e b7 de 00 00 8a 3e 
11:57:35.729 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][sdesCreationTest] rtcpPacketPaddingResult: {
  "length": 7,
  "paddingBytes": 3,
  "padding": true
}
11:57:35.731 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][sdesCreationTest] RtcpPacket: 
{
  "rtcpHeader": {
    "version": 2,
    "padding": 0,
    "paddingBytes": 3,
    "resourceCount": 1,
    "packetType": 202,
    "length": 7,
    "ssrc": 0
  },
  "rtcpFormat": {
    "sdesChunkList": [
      {
        "ssrc": 1569920308,
        "sdesItemList": [
          {
            "sdesType": "CNAME",
            "length": 5,
            "text": "CNAME"
          },
          {
            "sdesType": "PHONE",
            "length": 5,
            "text": "PHONE"
          },
          {
            "sdesType": "TOOL",
            "length": 4,
            "text": "TOOL"
          },
          {
            "sdesType": "END",
            "length": 0
          }
        ]
      }
    ]
  }
}
11:57:35.740 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][sdesCreationTest] RtcpPacketTest byte data: (size=32)
[-127, -54, 0, 7, 93, -109, 21, 52, 1, 5, 67, 78, 65, 77, 69, 4, 5, 80, 72, 79, 78, 69, 6, 4, 84, 79, 79, 76, 0, 0, 0, 0]
11:57:35.741 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][sdesCreationTest] RtcpPacketTest byte data: 
81 ca 00 07 5d 93 15 34 01 05 43 4e 41 4d 45 04 05 50 48 4f 4e 45 06 04 54 4f 4f 4c 00 00 00 00 
11:57:35.742 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][byeCreationTest] rtcpPacketPaddingResult: {
  "length": 3,
  "paddingBytes": 0,
  "padding": false
}
11:57:35.744 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][byeCreationTest] RtcpPacket: 
{
  "rtcpHeader": {
    "version": 2,
    "padding": 0,
    "paddingBytes": 0,
    "resourceCount": 1,
    "packetType": 203,
    "length": 3,
    "ssrc": 26422708
  },
  "rtcpFormat": {
    "length": 7,
    "reason": "GOODBYE"
  }
}
11:57:35.748 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][byeCreationTest] RtcpPacketTest byte data: (size=16)
[-127, -53, 0, 3, 1, -109, 45, -76, 7, 71, 79, 79, 68, 66, 89, 69]
11:57:35.750 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][byeCreationTest] RtcpPacketTest byte data: 
81 cb 00 03 01 93 2d b4 07 47 4f 4f 44 42 59 45 
11:57:35.761 [main] DEBUG instance.BaseEnvironment - [RtcpTest][multiRtcpPacketTest] rtcpCompoundPacket: {
  "rtcpPacketList": [
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
        "mswNts": 933148638,
        "lswNts": 933148638,
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
            "lsr": 933148638,
            "dlsr": 35390
          }
        ]
      }
    },
    {
      "rtcpHeader": {
        "version": 2,
        "padding": 0,
        "paddingBytes": 3,
        "resourceCount": 1,
        "packetType": 202,
        "length": 7,
        "ssrc": 0
      },
      "rtcpFormat": {
        "sdesChunkList": [
          {
            "ssrc": 1569920308,
            "sdesItemList": [
              {
                "sdesType": "CNAME",
                "length": 5,
                "text": "CNAME"
              },
              {
                "sdesType": "PHONE",
                "length": 5,
                "text": "PHONE"
              },
              {
                "sdesType": "TOOL",
                "length": 4,
                "text": "TOOL"
              },
              {
                "sdesType": "END",
                "length": 0
              }
            ]
          }
        ]
      }
    },
    {
      "rtcpHeader": {
        "version": 2,
        "padding": 0,
        "paddingBytes": 0,
        "resourceCount": 1,
        "packetType": 203,
        "length": 3,
        "ssrc": 26422708
      },
      "rtcpFormat": {
        "length": 7,
        "reason": "GOODBYE"
      }
    }
  ]
}
11:57:35.762 [main] DEBUG instance.BaseEnvironment - Success to send the data. (size=100)
11:57:35.763 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] remainDataLength: [100], index: [0]
11:57:35.763 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] RtcpHeader[8]: 
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
11:57:35.764 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] totalDataLength: [100], index: [8], PacketLength: [12], curRemainDataLength: [44]
11:57:35.764 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] RtcpPacket: (size=8+44)
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
    "mswNts": 933148638,
    "lswNts": 933148638,
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
        "lsr": 933148638,
        "dlsr": 35390
      }
    ]
  }
}
11:57:35.765 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] remainDataLength: [48], index: [52]
11:57:35.765 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] RtcpHeader[4]: 
[{
  "version": 2,
  "padding": 0,
  "paddingBytes": 0,
  "resourceCount": 1,
  "packetType": 202,
  "length": 7,
  "ssrc": 0
}]
index: [56]
11:57:35.765 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] totalDataLength: [100], index: [56], PacketLength: [7], curRemainDataLength: [28]
11:57:35.765 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] RtcpPacket: (size=4+28)
{
  "rtcpHeader": {
    "version": 2,
    "padding": 0,
    "paddingBytes": 0,
    "resourceCount": 1,
    "packetType": 202,
    "length": 7,
    "ssrc": 0
  },
  "rtcpFormat": {
    "sdesChunkList": [
      {
        "ssrc": 1569920308,
        "sdesItemList": [
          {
            "sdesType": "CNAME",
            "length": 5,
            "text": "CNAME"
          },
          {
            "sdesType": "PHONE",
            "length": 5,
            "text": "PHONE"
          },
          {
            "sdesType": "TOOL",
            "length": 4,
            "text": "TOOL"
          },
          {
            "sdesType": "END",
            "length": 0
          }
        ]
      }
    ]
  }
}
11:57:35.773 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] remainDataLength: [16], index: [84]
11:57:35.774 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] RtcpHeader[8]: 
[{
  "version": 2,
  "padding": 0,
  "paddingBytes": 0,
  "resourceCount": 1,
  "packetType": 203,
  "length": 3,
  "ssrc": 26422708
}]
index: [92]
11:57:35.775 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] totalDataLength: [100], index: [92], PacketLength: [3], curRemainDataLength: [8]
11:57:35.775 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] RtcpPacket: (size=8+8)
{
  "rtcpHeader": {
    "version": 2,
    "padding": 0,
    "paddingBytes": 0,
    "resourceCount": 1,
    "packetType": 203,
    "length": 3,
    "ssrc": 26422708
  },
  "rtcpFormat": {
    "length": 7,
    "reason": "GOODBYE"
  }
}
11:57:35.776 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] remainDataLength: [0], index: [100]
11:57:35.776 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] RtcpCompoundPacket: (size=97)
{
  "rtcpPacketList": [
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
        "mswNts": 933148638,
        "lswNts": 933148638,
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
            "lsr": 933148638,
            "dlsr": 35390
          }
        ]
      }
    },
    {
      "rtcpHeader": {
        "version": 2,
        "padding": 0,
        "paddingBytes": 0,
        "resourceCount": 1,
        "packetType": 202,
        "length": 7,
        "ssrc": 0
      },
      "rtcpFormat": {
        "sdesChunkList": [
          {
            "ssrc": 1569920308,
            "sdesItemList": [
              {
                "sdesType": "CNAME",
                "length": 5,
                "text": "CNAME"
              },
              {
                "sdesType": "PHONE",
                "length": 5,
                "text": "PHONE"
              },
              {
                "sdesType": "TOOL",
                "length": 4,
                "text": "TOOL"
              },
              {
                "sdesType": "END",
                "length": 0
              }
            ]
          }
        ]
      }
    },
    {
      "rtcpHeader": {
        "version": 2,
        "padding": 0,
        "paddingBytes": 0,
        "resourceCount": 1,
        "packetType": 203,
        "length": 3,
        "ssrc": 26422708
      },
      "rtcpFormat": {
        "length": 7,
        "reason": "GOODBYE"
      }
    }
  ]
}
~~~
  
