extends Node2D

var go_to_selectModeScene=preload("res://Myassets/Scenes/modeSelectorScene/modeSelector.tscn")
var go_to_RegisterScene=preload("res://Myassets/Scenes/RegisterAndLogin/register.tscn")
# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	checkApiStatus()


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta: float) -> void:
	pass


func _on_button_pressed() -> void:
	get_tree().change_scene_to_packed(go_to_selectModeScene)# Replace with function body.


func _on_button_2_pressed() -> void:
	get_tree().change_scene_to_packed(go_to_RegisterScene)


func checkApiStatus():
	var http = HTTPRequest.new()
	add_child(http)

	# Conectar para manejar la respuesta
	http.connect("request_completed", Callable(self, "_on_ping_response"))

	# Podés usar un endpoint simple tipo `/ping` o `/status`
	var url = "https://localhost:8080/api/ping"
	var error = http.request(url)
	print(error)
	if error != OK or error==0:
		print("Error al hacer ping a la API:", error)
		$apiEstatus.text = "❌ Error al conectar"
	else:
		$apiEstatus.text = "Conectado"
