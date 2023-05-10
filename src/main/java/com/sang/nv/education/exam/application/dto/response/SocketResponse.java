package com.sang.nv.education.exam.application.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class SocketResponse<D> implements Serializable {
    D data;
    Boolean status;

    public static <D> SocketResponse<D> ok(D res) {
        SocketResponse<D> response = new SocketResponse<>();
        response.data = res;
        response.status = true;
        return response;
    }

    public static <D> SocketResponse<D> fail(D res) {
        SocketResponse<D> response = new SocketResponse<>();
        response.data = res;
        response.status = false;
        return response;
    }
}
