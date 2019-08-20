package io.github.zeroone3010.yahueapi.domain

import com.fasterxml.jackson.annotation.JsonProperty

class BridgeConfig {
  @JsonProperty("name")
  val name: String? = null
  @JsonProperty("zigbeechannel")
  val zigbeeChannel: Int = 0
  @JsonProperty("bridgeid")
  val bridgeId: String? = null
  @JsonProperty("mac")
  val mac: String? = null
  @JsonProperty("dhcp")
  val isDhcp: Boolean = false
  @JsonProperty("ipaddress")
  val ipAddress: String? = null
  @JsonProperty("netmask")
  val netmask: String? = null
  @JsonProperty("gateway")
  val gateway: String? = null
  @JsonProperty("proxyaddress")
  val proxyAddress: String? = null
  @JsonProperty("proxyport")
  val proxyPort: Int = 0
  @JsonProperty("UTC")
  val utc: String? = null
  @JsonProperty("localtime")
  val localTime: String? = null
  @JsonProperty("timezone")
  val timeZone: String? = null
  @JsonProperty("modelid")
  val modelId: String? = null
  @JsonProperty("datastoreversion")
  val dataStoreVersion: String? = null
  @JsonProperty("swversion")
  val softwareVersion: String? = null
  @JsonProperty("apiversion")
  val apiVersion: String? = null
  @JsonProperty("swupdate")
  val softwareUpdate: ConfigSoftwareUpdate? = null
  @JsonProperty("swupdate2")
  val softwareUpdate2: ConfigSoftwareUpdate2? = null
  @JsonProperty("linkbutton")
  val isLinkButton: Boolean = false
  @JsonProperty("portalservices")
  val isPortalServices: Boolean = false
  @JsonProperty("portalconnection")
  val portalConnection: String? = null
  @JsonProperty("portalstate")
  val portalState: PortalState? = null
  @JsonProperty("internetservices")
  val internetServices: InternetServices? = null
  @JsonProperty("factorynew")
  val isFactoryNew: Boolean = false
  @JsonProperty("replacesbridgeid")
  val replacesBridgeId: String? = null
  @JsonProperty("backup")
  val backup: ConfigBackup? = null
  @JsonProperty("starterkitid")
  val starterKitId: String? = null
  @JsonProperty("whitelist")
  val whiteList: Map<String, WhiteListItem>? = null

  override fun toString(): String {
    return JsonStringUtil.toJsonString(this)
  }
}
