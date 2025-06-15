package mp.iamuproject.api

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "item", strict = false)
class HNItem{
    @field:Element(name = "title") var title: String? = null
    @field:Element(name = "description") var description: String? = null
    @field:Element(name = "link") var link: String? = null
    @field:Element(name = "pubDate") var pubDate: String? = null
    @field:Element(name = "author", required = false) var author: String? = null
    @field:Element(name = "guid") var guid: String? = null
    @field:Element(name = "enclosure", required = false) var enclosure: Enclosure? = null
}

@Root(name = "enclosure", strict = false)
class Enclosure {
    @field:Attribute(name = "url", required = false) var url: String? = null
    @field:Attribute(name = "type", required = false) var type: String? = null
    @field:Attribute(name = "length", required = false) var length: Long? = null
}