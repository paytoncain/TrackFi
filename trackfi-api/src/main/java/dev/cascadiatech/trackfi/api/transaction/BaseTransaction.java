package dev.cascadiatech.trackfi.api.transaction;

import java.time.LocalDate;

/**
 * Defines a financial transaction
 */
interface BaseTransaction {

  /**
   * Storage id
   */
  Integer id();

  /**
   * User storage id
   */
  Integer userId();

  /**
   * Vendor name
   */
  String vendor();

  /**
   * Transaction amount
   */
  Float amount();

  /**
   * Transaction date
   */
  LocalDate date();

}
