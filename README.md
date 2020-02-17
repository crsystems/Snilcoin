# Snilcoin
Just another Blockchain Cryptocurrency

### Communication packet structure
    ------------------------------------------------
    |    1 byte    |     4 byte     | length bytes |
    ------------------------------------------------
    | Payload type | Payload length |    Payload   |
    ------------------------------------------------

#### Payload type 1 - Hostlist exchange
    -----------------------------------------------------------------------------------------------------------------------
    |  0b00000001  |     n * 6      | 4 bytes ip address 1 | 2 bytes port 1 | ... | 4 bytes ip address n | 2 bytes port n |
    -----------------------------------------------------------------------------------------------------------------------
