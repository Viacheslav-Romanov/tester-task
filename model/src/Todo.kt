data class Todo(val id: Int, val text: String, val completed: Boolean) {
    override fun toString(): String =
        """{"id": $id, "text": "$text", "completed": $completed}"""
}
