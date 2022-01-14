# JRTCP
## RTCP Stack
~~~
1) Netty 사용
2) RTP 기반 테스트 활용
3) Single or Compound 패킷 테스트 가능 (by wireshark)
4) Restcomm 오픈 소스 활용
    - RTP Clock
    - RTP Statistics
    - NTP handling
5) [EXTENDED] RTCP Feedback Message 지원 (for video streaming) (개발 중)
    - Transport-layer
        - NACK
        - TMMBR
        - TMMBN
    - Payload-specific
        - PLI
        - SLI
        - RPSI
        - FIR
        - TSTR
        - TSTN
        - VBCM
        - AFB
6) [EXTENDED] RTCP Feedback Information 을 이용한 RTP PAUSE, RESUME 기능 지원 (개발 중)
    - PAUSE
    - RESUME
    - PAUSED
    - REFUSED
~~~

### Reference
#### RTCP REGULAR
https://www4.cs.fau.de/Projects/JRTP/pmt/node82.html  
https://www.freesoft.org/CIE/RFC/1889/13.htm  
https://datatracker.ietf.org/doc/html/rfc1889  

#### RTCP EXTENDED
##### Feedback message
https://datatracker.ietf.org/doc/html/rfc4585  
https://datatracker.ietf.org/doc/html/rfc5104  
https://datatracker.ietf.org/doc/html/rfc2032  
  
##### Feedback Information for RTP Control (PAUSE, RESUME, etc)
https://datatracker.ietf.org/doc/html/rfc7728  
  

#### 오픈 소스
https://github.com/RestComm/media-core
  
### Example (RFC 1889)
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
12:40:38.721 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][srCreationTest] rtcpPacketPaddingResult: {
  "length": 12,
  "paddingBytes": 0,
  "padding": false
}
12:40:38.723 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][srCreationTest] RtcpPacket: 
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
    "mswNts": 935731649,
    "lswNts": 935731649,
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
        "lsr": 935731649,
        "dlsr": 35390
      }
    ]
  }
}
12:40:38.731 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][srCreationTest] RtcpPacketTest byte data: (size=52)
[-127, -56, 0, 12, 1, -109, 45, -76, 55, -58, 33, -63, 55, -58, 33, -63, 0, 3, -44, 0, 0, 0, 6, 32, 0, 0, 9, -52, 93, -109, 21, 52, 0, 0, 0, 1, 0, 0, -58, -1, 0, 0, 0, 76, 55, -58, 33, -63, 0, 0, -118, 62]
12:40:38.733 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][srCreationTest] RtcpPacketTest byte data: 
81 c8 00 0c 01 93 2d b4 37 c6 21 c1 37 c6 21 c1 00 03 d4 00 00 00 06 20 00 00 09 cc 5d 93 15 34 00 00 00 01 00 00 c6 ff 00 00 00 4c 37 c6 21 c1 00 00 8a 3e 
12:40:38.740 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][sdesCreationTest] rtcpPacketPaddingResult: {
  "length": 18,
  "paddingBytes": 0,
  "padding": false
}
12:40:38.741 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][sdesCreationTest] RtcpPacket: 
{
  "rtcpHeader": {
    "version": 2,
    "padding": 0,
    "paddingBytes": 0,
    "resourceCount": 3,
    "packetType": 202,
    "length": 18,
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
      },
      {
        "ssrc": 26422708,
        "sdesItemList": [
          {
            "sdesType": "CNAME",
            "length": 5,
            "text": "CNAME"
          },
          {
            "sdesType": "LOC",
            "length": 3,
            "text": "LOC"
          },
          {
            "sdesType": "EMAIL",
            "length": 5,
            "text": "EMAIL"
          },
          {
            "sdesType": "END",
            "length": 0
          }
        ]
      },
      {
        "ssrc": 328590819,
        "sdesItemList": [
          {
            "sdesType": "CNAME",
            "length": 5,
            "text": "CNAME"
          },
          {
            "sdesType": "LOC",
            "length": 3,
            "text": "LOC"
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
12:40:38.748 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][sdesCreationTest] RtcpPacketTest byte data: (size=76)
[-125, -54, 0, 18, 93, -109, 21, 52, 1, 5, 67, 78, 65, 77, 69, 4, 5, 80, 72, 79, 78, 69, 6, 4, 84, 79, 79, 76, 0, 0, 0, 0, 1, -109, 45, -76, 1, 5, 67, 78, 65, 77, 69, 5, 3, 76, 79, 67, 3, 5, 69, 77, 65, 73, 76, 0, 19, -107, -27, -29, 1, 5, 67, 78, 65, 77, 69, 5, 3, 76, 79, 67, 0, 0, 0, 0]
12:40:38.755 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][sdesCreationTest] RtcpPacketTest byte data: 
83 ca 00 12 5d 93 15 34 01 05 43 4e 41 4d 45 04 05 50 48 4f 4e 45 06 04 54 4f 4f 4c 00 00 00 00 01 93 2d b4 01 05 43 4e 41 4d 45 05 03 4c 4f 43 03 05 45 4d 41 49 4c 00 13 95 e5 e3 01 05 43 4e 41 4d 45 05 03 4c 4f 43 00 00 00 00 
12:40:38.755 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][byeCreationTest] rtcpPacketPaddingResult: {
  "length": 3,
  "paddingBytes": 0,
  "padding": false
}
12:40:38.757 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][byeCreationTest] RtcpPacket: 
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
12:40:38.758 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][byeCreationTest] RtcpPacketTest byte data: (size=16)
[-127, -53, 0, 3, 1, -109, 45, -76, 7, 71, 79, 79, 68, 66, 89, 69]
12:40:38.758 [main] DEBUG rtcp.RtcpPacketTest - [RtcpPacketTest][byeCreationTest] RtcpPacketTest byte data: 
81 cb 00 03 01 93 2d b4 07 47 4f 4f 44 42 59 45 
12:40:38.765 [main] DEBUG instance.BaseEnvironment - [RtcpTest][multiRtcpPacketTest] rtcpCompoundPacket: {
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
        "mswNts": 935731649,
        "lswNts": 935731649,
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
            "lsr": 935731649,
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
        "resourceCount": 3,
        "packetType": 202,
        "length": 18,
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
          },
          {
            "ssrc": 26422708,
            "sdesItemList": [
              {
                "sdesType": "CNAME",
                "length": 5,
                "text": "CNAME"
              },
              {
                "sdesType": "LOC",
                "length": 3,
                "text": "LOC"
              },
              {
                "sdesType": "EMAIL",
                "length": 5,
                "text": "EMAIL"
              },
              {
                "sdesType": "END",
                "length": 0
              }
            ]
          },
          {
            "ssrc": 328590819,
            "sdesItemList": [
              {
                "sdesType": "CNAME",
                "length": 5,
                "text": "CNAME"
              },
              {
                "sdesType": "LOC",
                "length": 3,
                "text": "LOC"
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
12:40:38.766 [main] DEBUG instance.BaseEnvironment - Success to send the data. (size=144)
12:40:38.767 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] remainDataLength: [144], index: [0]
12:40:38.767 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] RtcpHeader[8]: 
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
12:40:38.770 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] totalDataLength: [144], index: [8], PacketLength: [12], curRemainDataLength: [44]
12:40:38.770 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] RtcpPacket: (size=8+44)
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
    "mswNts": 935731649,
    "lswNts": 935731649,
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
        "lsr": 935731649,
        "dlsr": 35390
      }
    ]
  }
}
12:40:38.776 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] remainDataLength: [92], index: [52]
12:40:38.776 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] RtcpHeader[4]: 
[{
  "version": 2,
  "padding": 0,
  "paddingBytes": 0,
  "resourceCount": 1,
  "packetType": 202,
  "length": 18,
  "ssrc": 0
}]
index: [56]
12:40:38.777 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] totalDataLength: [144], index: [56], PacketLength: [18], curRemainDataLength: [72]
12:40:38.777 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] RtcpPacket: (size=4+72)
{
  "rtcpHeader": {
    "version": 2,
    "padding": 0,
    "paddingBytes": 0,
    "resourceCount": 1,
    "packetType": 202,
    "length": 18,
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
      },
      {
        "ssrc": 26422708,
        "sdesItemList": [
          {
            "sdesType": "CNAME",
            "length": 5,
            "text": "CNAME"
          },
          {
            "sdesType": "LOC",
            "length": 3,
            "text": "LOC"
          },
          {
            "sdesType": "EMAIL",
            "length": 5,
            "text": "EMAIL"
          },
          {
            "sdesType": "END",
            "length": 0
          }
        ]
      },
      {
        "ssrc": 328590819,
        "sdesItemList": [
          {
            "sdesType": "CNAME",
            "length": 5,
            "text": "CNAME"
          },
          {
            "sdesType": "LOC",
            "length": 3,
            "text": "LOC"
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
12:40:38.784 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] remainDataLength: [16], index: [128]
12:40:38.784 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] RtcpHeader[8]: 
[{
  "version": 2,
  "padding": 0,
  "paddingBytes": 0,
  "resourceCount": 1,
  "packetType": 203,
  "length": 3,
  "ssrc": 26422708
}]
index: [136]
12:40:38.786 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] totalDataLength: [144], index: [136], PacketLength: [3], curRemainDataLength: [8]
12:40:38.786 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] RtcpPacket: (size=8+8)
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
12:40:38.787 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] remainDataLength: [0], index: [144]
12:40:38.787 [nioEventLoopGroup-6-1] DEBUG network.rtcp.handler.RtcpServerHandler - [RtcpServerHandler<RECEIVER/127.0.0.1:5000>] RtcpCompoundPacket: (size=144)
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
        "mswNts": 935731649,
        "lswNts": 935731649,
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
            "lsr": 935731649,
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
        "length": 18,
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
          },
          {
            "ssrc": 26422708,
            "sdesItemList": [
              {
                "sdesType": "CNAME",
                "length": 5,
                "text": "CNAME"
              },
              {
                "sdesType": "LOC",
                "length": 3,
                "text": "LOC"
              },
              {
                "sdesType": "EMAIL",
                "length": 5,
                "text": "EMAIL"
              },
              {
                "sdesType": "END",
                "length": 0
              }
            ]
          },
          {
            "ssrc": 328590819,
            "sdesItemList": [
              {
                "sdesType": "CNAME",
                "length": 5,
                "text": "CNAME"
              },
              {
                "sdesType": "LOC",
                "length": 3,
                "text": "LOC"
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
  
### Example (RFC 5104, 4585, 2032)
  
