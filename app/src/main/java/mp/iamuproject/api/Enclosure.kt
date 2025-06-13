package mp.iamuproject.api

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root

@Root(name = "enclosure", strict = false)
class Enclosure {
    @field:Attribute(name = "url", required = false) var url: String? = null
    @field:Attribute(name = "type", required = false) var type: String? = null
    @field:Attribute(name = "length", required = false) var length: Long? = null
}