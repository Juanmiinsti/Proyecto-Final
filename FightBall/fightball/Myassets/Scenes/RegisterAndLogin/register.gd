extends Control

@onready var username_input = $Username
@onready var password_input = $Password
@onready var confirm_input = $ConfirmPassword

func _ready() -> void:
	# Asignamos la ruta de esta escena para que funcione con SceneManager
	set_meta("scene_path", "res://Myassets/Scenes/RegisterAndLogin/register.tscn")

func _on_button_register_pressed() -> void:
	# Limpiamos errores previos (en este caso solo por consola)
	print("🔁 Intentando registrar nuevo usuario...")

	# Obtenemos los valores de los campos
	var username = username_input.text.strip_edges()
	var password = password_input.text.strip_edges()
	var confirm_password = confirm_input.text.strip_edges()

	# Validación básica: campos vacíos
	if username.is_empty() or password.is_empty() or confirm_password.is_empty():
		print("❗ Todos los campos son obligatorios.")
		return

	# Validación: contraseñas deben coincidir
	if password != confirm_password:
		print("❗ Las contraseñas no coinciden.")
		return

	# Creamos el nodo HTTPRequest (componente de Godot para hacer peticiones web)
	var http = HTTPRequest.new()
	add_child(http)  # Importante: se debe agregar al árbol de nodos

	http.timeout = 5  # Tiempo máximo de espera (en segundos)
	http.request_completed.connect(_on_register_response)  # Conectamos la señal de respuesta

	# URL del backend para el registro
	var url = PlayerInfo.urlSpring+"auth/signup"

	# Encabezados HTTP que indican que estamos enviando JSON
	var headers = ["Content-Type: application/json"]

	# Creamos el cuerpo del mensaje en formato JSON con los datos del usuario
	var request_body = JSON.stringify({
		"name": username,
		"password": password
	})

	# Enviamos la petición POST al servidor
	var err = http.request(url, headers, HTTPClient.METHOD_POST, request_body)
	if err != OK:
		print("❌ Error al iniciar la petición HTTP. Código de error:", err)
		http.queue_free()

# Función que se ejecuta cuando llega la respuesta del servidor
func _on_register_response(result: int, response_code: int, headers: PackedStringArray, body: PackedByteArray):
	# Leemos la respuesta del servidor (puede contener info útil)
	var response_text = body.get_string_from_utf8()
	print("📨 Respuesta del servidor:", response_code, response_text)

	# Código 201 = Created (registro exitoso)
	if response_code == 201:
		print("✅ ¡Usuario registrado con éxito!")

		# Limpiamos los campos
		username_input.text = ""
		password_input.text = ""
		confirm_input.text = ""

		# Esperamos un momento y luego volvemos a la pantalla anterior (login)
		await get_tree().create_timer(1.5).timeout
		SceneManager.go_back()
	else:
		# Si algo salió mal, mostramos el código de error y el mensaje
		print("❌ Registro fallido. Código HTTP:", response_code)
		print("⚠️ Detalles del error:", response_text)

# Función del botón para volver atrás (al login)
func _on_button_back_pressed() -> void:
	SceneManager.go_back()
