from pyroute2.netlink import NLM_F_REQUEST
from pyroute2.netlink import NLM_F_ACK
from pyroute2.netlink import NLM_F_CREATE
from pyroute2.netlink import NLM_F_EXCL
from pyroute2.iproute import RTM_NEWADDR
from pyroute2.netlink.rtnl.ifaddrmsg import ifaddrmsg
import os

##
# add an addr to an interface
#

# create the message
msg = ifaddrmsg()

# fill the protocol-specific fields
msg['index'] = index  # index of the interface
msg['family'] = AF_INET  # address family
msg['prefixlen'] = 24  # the address mask
msg['scope'] = scope  # see /etc/iproute2/rt_scopes

# attach NLA -- it MUST be a list / mutable
msg['attrs'] = [['IFA_LOCAL', '192.168.0.1'],
                ['IFA_ADDRESS', '192.162.0.1']]

# fill generic netlink fields
msg['header']['sequence_number'] = nonce  # an unique seq number
msg['header']['pid'] = os.getpid()
msg['header']['type'] = RTM_NEWADDR
msg['header']['flags'] = NLM_F_REQUEST |\
                         NLM_F_ACK |\
                         NLM_F_CREATE |\
                         NLM_F_EXCL

# encode the packet
msg.encode()

# send the buffer
nlsock.sendto(msg.data, (0, 0))