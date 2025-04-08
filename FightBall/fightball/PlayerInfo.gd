extends Node
var userId:int
var userPassword:String
var userName:String


# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	pass # Replace with function body.


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta: float) -> void:
	pass
	
	
func registerUser(username: String, password: String) -> void:
	var http = HTTPRequest.new()
	add_child(http)

	# Conectamos la señal para saber si se completó
	http.connect("request_completed", Callable(self, "_on_register_request_completed"))

	var url = "http://localhost:8080/api/user"  # <-- Cambiá esto por la URL real

	var headers = ["Content-Type: application/json"]
	var body = {
		"name": username,
		"password": password
	}
	var json_body = JSON.stringify(body)

	var error = http.request(url, headers, HTTPClient.METHOD_POST, json_body)

	if error != OK:
		print("Error al enviar la solicitud de registro:", error)
