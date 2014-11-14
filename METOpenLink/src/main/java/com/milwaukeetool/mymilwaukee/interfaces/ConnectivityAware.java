package com.milwaukeetool.mymilwaukee.interfaces;

/**
 * Created by scott.hopfensperger on 11/13/2014.
 */
public interface ConnectivityAware {
    void connectionEstablished();
    void connectionDestroyed();
}
