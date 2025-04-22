extends Node

signal session_ready

var userKey: String = ""
var userName: String = ""
var userPassword: String = ""
var userID: int = 0  # ✅ Nuevo campo
var status: bool = false
var current_room_id: int = -1


func load_user_data():
	var path = "user://user_login_data.save"
	if FileAccess.file_exists(path):
		var file = FileAccess.open(path, FileAccess.READ)
		var content = file.get_as_text()
		file.close()

		var parsed = JSON.parse_string(content)
		if parsed:
			userName = parsed["userName"]
			userPassword = parsed["userPassword"]
			userKey = parsed["userKey"]
			userID = parsed.get("userID", 0)  # <- Usamos 0 por defecto si no estaba guardado
			print("✅ Sesión cargada para:", userName)
			session_ready.emit()
		else:
			print("⚠️ Error al parsear datos de sesión")

func save_user_data():
	var data = {
		"userName": userName,
		"userPassword": userPassword,
		"userKey": userKey,
		"userID": userID  # ✅ Guardamos el ID también
	}
	var file = FileAccess.open("user://user_login_data.save", FileAccess.WRITE)
	file.store_string(JSON.stringify(data))
	file.close()

func clear_session():
	var path = "user://user_login_data.save"
	if FileAccess.file_exists(path):
		DirAccess.remove_absolute(path)

	userName = ""
	userPassword = ""
	userKey = ""
	userID = 0
	print("🧹 Sesión eliminada")
