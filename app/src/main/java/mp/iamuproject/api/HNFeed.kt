package mp.iamuproject.api

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "rss", strict = false)
class HNFeed {
    @field:Element(name = "channel") var channel: HNChannel? = null
}

@Root(name = "channel", strict = false)
class HNChannel {
    @field:ElementList(inline = true, entry = "item") var items: List<HNItem>? = null
}