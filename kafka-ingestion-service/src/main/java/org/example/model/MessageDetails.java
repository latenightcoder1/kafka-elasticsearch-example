package org.example.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDetails implements Serializable {

    private String message;
    private long timestamp;
    private String username;

}
