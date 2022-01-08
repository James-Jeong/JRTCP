# JRTCP
## RTCP Module
  
https://www4.cs.fau.de/Projects/JRTP/pmt/node82.html
  
https://www.freesoft.org/CIE/RFC/1889/13.htm
  
https://datatracker.ietf.org/doc/html/rfc1889
  
## Example
### Single packet
  
11:31:57.935 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][srCreationTest] RtcpPacket:  
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
    "mswNts": 931610832,  
    "lswNts": 931610831,  
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
        "lsr": 931610831,  
        "dlsr": 35390  
      }  
    ]  
  }  
}  
11:31:57.940 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][srCreationTest] RtcpPacketTest byte data: (size=52)  
[-127, -56, 0, 12, 1, -109, 45, -76, 55, -121, 64, -48, 55, -121, 64, -49, 0, 3, -44, 0, 0, 0, 6, 32, 0, 0, 9, -52, 93, -109, 21, 52, 0, 0, 0, 1, 0, 0, -58, -1, 0, 0, 0, 76, 55, -121, 64, -49, 0, 0, -118, 62]  
11:31:57.944 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][srCreationTest] RtcpPacketTest byte data:   
81 c8 00 0c 01 93 2d b4 37 87 40 d0 37 87 40 cf 00 03 d4 00 00 00 06 20 00 00 09 cc 5d 93 15 34 00 00 00 01 00 00 c6 ff 00 00 00 4c 37 87 40 cf 00 00 8a 3e   
11:31:57.955 [main] DEBUG instance.BaseEnvironment - Success to send the data. (size=52)  
  
11:31:57.966 [nioEventLoopGroup-3-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] remainDataLength: [52], index: [0]  
11:31:57.967 [nioEventLoopGroup-3-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] RtcpHeader[8]:   
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
11:31:57.968 [nioEventLoopGroup-3-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] totalDataLength: [52], index: [8],   PacketLength: [12], curRemainDataLength: [44]  
11:31:57.968 [nioEventLoopGroup-3-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] RtcpPacket: (size=8+44)  
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
    "mswNts": 931610832,  
    "lswNts": 931610831,  
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
        "lsr": 931610831,  
        "dlsr": 35390  
      }  
    ]  
  }  
}  
11:31:57.971 [nioEventLoopGroup-3-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] remainDataLength: [0], index: [52]  
  
### Compound packet
  
11:31:59.033 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][srCreationTest] RtcpPacket:   
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
    "mswNts": 931611957,  
    "lswNts": 931611957,  
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
        "lsr": 931611957,  
        "dlsr": 35390  
      }  
    ]  
  }  
}  
11:31:59.035 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][srCreationTest] RtcpPacketTest byte data: (size=52)  
[-127, -56, 0, 12, 1, -109, 45, -76, 55, -121, 69, 53, 55, -121, 69, 53, 0, 3, -44, 0, 0, 0, 6, 32, 0, 0, 9, -52, 93, -109, 21, 52, 0, 0, 0, 1, 0, 0, -58, -1, 0, 0, 0, 76, 55, -121, 69, 53, 0, 0, -118, 62]  
11:31:59.037 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][srCreationTest] RtcpPacketTest byte data:   
81 c8 00 0c 01 93 2d b4 37 87 45 35 37 87 45 35 00 03 d4 00 00 00 06 20 00 00 09 cc 5d 93 15 34 00 00 00 01 00 00 c6 ff 00 00 00 4c 37 87 45 35 00 00 8a 3e   
11:31:59.044 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][sdesCreationTest] rtcpPacketPaddingResult: {  
  "length": 7,  
  "paddingBytes": 3,  
  "padding": true  
}  
11:31:59.046 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][sdesCreationTest] RtcpPacket:   
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
11:31:59.055 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][sdesCreationTest] RtcpPacketTest byte data: (size=32)  
[-127, -54, 0, 7, 93, -109, 21, 52, 1, 5, 67, 78, 65, 77, 69, 4, 5, 80, 72, 79, 78, 69, 6, 4, 84, 79, 79, 76, 0, 0, 0, 0]  
11:31:59.059 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][sdesCreationTest] RtcpPacketTest byte data:   
81 ca 00 07 5d 93 15 34 01 05 43 4e 41 4d 45 04 05 50 48 4f 4e 45 06 04 54 4f 4f 4c 00 00 00 00   
11:31:59.059 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][byeCreationTest] rtcpPacketPaddingResult: {  
  "length": 3,  
  "paddingBytes": 0,  
  "padding": false  
}  
11:31:59.060 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][byeCreationTest] RtcpPacket:   
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
11:31:59.061 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][byeCreationTest] RtcpPacketTest byte data: (size=16)  
[-127, -53, 0, 3, 1, -109, 45, -76, 7, 71, 79, 79, 68, 66, 89, 69]  
11:31:59.062 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][byeCreationTest] RtcpPacketTest byte data:   
81 cb 00 03 01 93 2d b4 07 47 4f 4f 44 42 59 45   
11:31:59.076 [main] DEBUG instance.BaseEnvironment - [RtcpTest][multiRtcpPacketTest] rtcpCompoundPacket: {  
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
        "mswNts": 931611957,  
        "lswNts": 931611957,  
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
            "lsr": 931611957,  
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
11:31:59.079 [main] DEBUG instance.BaseEnvironment - Success to send the data. (size=100)  
  
11:31:59.080 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] remainDataLength: [100], index: [0]  
11:31:59.080 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] RtcpHeader[8]:   
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
11:31:59.081 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] totalDataLength: [100], index: [8], PacketLength: [12], curRemainDataLength: [44]  
11:31:59.081 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] RtcpPacket: (size=8+44)  
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
    "mswNts": 931611957,  
    "lswNts": 931611957,  
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
        "lsr": 931611957,  
        "dlsr": 35390  
      }  
    ]  
  }  
}  
11:31:59.083 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] remainDataLength: [48], index: [52]  
11:31:59.084 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] RtcpHeader[4]:   
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
11:31:59.084 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] totalDataLength: [100], index: [56], PacketLength: [7], curRemainDataLength: [28]  
11:31:59.084 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] RtcpPacket: (size=4+28)  
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
11:31:59.091 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] remainDataLength: [16], index: [84]  
11:31:59.092 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] RtcpHeader[8]:   
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
11:31:59.094 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] totalDataLength: [100], index: [92], PacketLength: [3], curRemainDataLength: [8]  
11:31:59.094 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] RtcpPacket: (size=8+8)  
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
11:31:59.095 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] remainDataLength: [0], index: [100]  
11:31:59.096 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] RtcpCompoundPacket: (size=97)  
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
        "mswNts": 931611957,  
        "lswNts": 931611957,  
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
            "lsr": 931611957,  
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

