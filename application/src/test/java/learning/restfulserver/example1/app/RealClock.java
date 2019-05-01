package learning.restfulserver.example1.app;

import learning.restfulserver.example1.resources.Clock;

import java.util.Date;

public class RealClock implements Clock {

  @Override
  public Date now() {
    return new Date();
  }
}
