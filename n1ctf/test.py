from scapy.all import *
import os
FIN = 0x01
SYN = 0x02
RST = 0x04
PSH = 0x08
ACK = 0x10
URG = 0x20
ECE = 0x40
CWR = 0x80
outs2= PcapWriter("output2.pcap", sync=True)
outs3= PcapWriter("output3.pcap", sync=True)
with PcapWriter("output.pcap", sync=True) as outs:
    with PcapReader("input.pcap") as ins:
        i=0
        j=0
        response_payload=None
        packet_payload=[]
        flag=0
        for pkt in ins:
            response_payload_len=0
            if TCP in pkt:
                #print(type(packet))
                response_sequence_number = pkt[TCP].seq
                F = pkt['TCP'].flags
                response_acknowledgement_number = pkt[TCP].ack
                response_timestamp = pkt[TCP].time
                response_payload_len += len(pkt[TCP].payload)
                tcp_sport=pkt[TCP].sport
                tcp_dport=pkt[TCP].dport
                if tcp_sport==9999:
                    if (F & FIN):
                        flag=1
                    if F & PSH:
                        response_payload = pkt[TCP].payload
                        packet_payload.append(pkt)
                if tcp_dport==9999:
                    if F & PSH:
                        response_payload = pkt[TCP].payload
                        packet_payload.append(pkt)
                        #print(len(packet_payload))
                    if (F & FIN) and (response_payload) and not(flag):
                        i=i+1
                        #print(response_sequence_number)
                        #print(response_acknowledgement_number)
                        #print(response_timestamp)
                        #print(response_payload_len)
                        #print(response_payload)
                        #print(type(response_payload))
                        with open(f"./client/{i}","wb") as f:
                            print(f"i={i}")
                            f.write(bytes(response_payload))
                            f.close()

                        with open(f"client_hex/{i}","wb") as f:
                            os.system(f"hexdump -C client/{i} > client_hex/{i}")
                        try:
                            outs.write(packet_payload[len(packet_payload)-1])
                            outs3.write(response_payload)
                        except:
                            print(len(packet_payload))
                        outs.write(pkt)
                        response_payload=None
                        flag=0
                    elif (F & FIN) and (response_payload) and (flag):
                        j=j+1
                        #print(response_sequence_number)
                        #print(response_acknowledgement_number)
                        #print(response_timestamp)
                        #print(response_payload_len)
                        #print(response_payload)
                        #print(type(response_payload))
                        with open(f"./server/{j}","wb") as f:
                            print(f"j={j}")
                            f.write(bytes(response_payload))
                            f.close()

                        with open(f"server_hex/{j}","wb") as f:
                            os.system(f"hexdump -C server/{j} > server_hex/{j}")
                        try:
                            outs2.write(packet_payload[len(packet_payload)-1])
                        except:
                            print(len(packet_payload))
                        outs2.write(pkt)
                        response_payload=None
                        flag=0