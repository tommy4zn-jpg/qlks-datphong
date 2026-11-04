package qlks.dao;

import java.util.List;
import qlks.exception.QLKSException;

public interface IDAO<T> {
    List<T> layTatCa() throws QLKSException;
    boolean them(T doiTuong) throws QLKSException;
    boolean capNhat(T doiTuong) throws QLKSException;
    boolean xoa(String ma) throws QLKSException;
}
