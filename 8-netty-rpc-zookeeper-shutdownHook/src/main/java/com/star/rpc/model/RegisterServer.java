package com.star.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author star
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterServer {
    String interfaceName;
    String host;
    int port;
}
