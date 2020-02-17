# Snilcoin
Just another Blockchain Cryptocurrency

### Communication packet structure
    ------------------------------------------------
    |    1 byte    |    4 bytes     | length bytes |
    ------------------------------------------------
    | Payload type | Payload length |    Payload   |
    ------------------------------------------------

#### Payload type 1 - Sync request
    ------------------------------------------------
    |     0x01     |   0x00000000   |  nonexistent |
    ------------------------------------------------

#### Payload type 2 - Hostlist exchange
    -----------------------------------------------------------------------------------------------------------------------
    |     0x02     |     n * 6      | 4 bytes ip address 1 | 2 bytes port 1 | ... | 4 bytes ip address n | 2 bytes port n |
    -----------------------------------------------------------------------------------------------------------------------

