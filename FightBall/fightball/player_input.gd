extends MultiplayerSynchronizer

@export var jumping:=false;
@export var input_direction=Vector2()
# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	set_process(get_multiplayer_authority()==multiplayer.get_unique_id()) # Replace with function body.
	

# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta: float) -> void:
	input_direction=Input.get_vector("left2player","down2player","right2player","pegar")
	if Input.is_action_just_pressed("Jump"):
		jump.rpc()

@rpc("call_local")
func jump():
	jumping=true
