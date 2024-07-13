package domain

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

/**
 * @author riezky maisyar
 */

class TodoTask : RealmObject {
    @PrimaryKey var _id: ObjectId = ObjectId()
    var title: String = ""
    var description: String = ""
    var favorite: Boolean = false
    var completed: Boolean = false
}